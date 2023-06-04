package me.cephetir.bladecore.config.settings

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import gg.essential.vigilance.data.Category
import me.cephetir.bladecore.BladeCore
import me.cephetir.bladecore.config.gui.ConfigGui
import me.cephetir.bladecore.config.settings.impl.*
import me.cephetir.bladecore.utils.minecraft.GuiUtils
import java.io.File
import java.util.*

class SettingManager(private val cfgFile: File, val name: String = "", val categorySorter: CategorySorting = CategorySorting()) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    private val settings = LinkedList<AbstractSetting<*>>()
    private val categories = PriorityQueue(categorySorter.getCategoryComparator())

    fun openGui() {
        GuiUtils.openScreen(ConfigGui(this))
    }

    fun booleanSetting(name: String, builder: BooleanSetting.() -> Unit): BooleanSetting {
        val setting = BooleanSetting(name).apply(builder)
        setting.checkValues()
        registerSetting(setting)
        return setting
    }

    fun numberSetting(name: String, builder: NumberSetting.() -> Unit): NumberSetting {
        val setting = NumberSetting(name).apply(builder)
        setting.checkValues()
        registerSetting(setting)
        return setting
    }

    fun selectorSetting(name: String, builder: SelectorSetting.() -> Unit): SelectorSetting {
        val setting = SelectorSetting(name).apply(builder)
        setting.checkValues()
        registerSetting(setting)
        return setting
    }

    fun buttonSetting(name: String, builder: ButtonSetting.() -> Unit): ButtonSetting {
        val setting = ButtonSetting(name).apply(builder)
        setting.checkValues()
        registerSetting(setting)
        return setting
    }

    fun textSetting(name: String, builder: TextSetting.() -> Unit): TextSetting {
        val setting = TextSetting(name).apply(builder)
        setting.checkValues()
        registerSetting(setting)
        return setting
    }

    fun keybindSetting(name: String, builder: KeybindSetting.() -> Unit): KeybindSetting {
        val setting = KeybindSetting(name).apply(builder)
        setting.checkValues()
        registerSetting(setting)
        return setting
    }

    fun decoratorSetting(name: String, builder: DecoratorSetting.() -> Unit): DecoratorSetting {
        val setting = DecoratorSetting(name).apply(builder)
        setting.checkValues()
        registerSetting(setting)
        return setting
    }

    private fun registerSetting(setting: AbstractSetting<*>) {
        settings.add(setting)

        val category = categories.find { it.name == setting.category }
        if (category == null)
            categories.add(Category(setting.category, PriorityQueue(categorySorter.getSubcategoryComparator()).apply {
                add(SubCategory(setting.subCategory, LinkedList<AbstractSetting<*>>().apply {
                    add(setting)
                }))
            }))
        else {
            val subCategory = category.subCategories.find { it.name == setting.subCategory }
            if (subCategory == null) {
                category.subCategories.add(SubCategory(setting.subCategory, LinkedList<AbstractSetting<*>>().apply {
                    add(setting)
                }))
            } else subCategory.settings.add(setting)
        }
    }

    fun getAllSettings(): List<AbstractSetting<*>> {
        val newList = mutableListOf<AbstractSetting<*>>()
        newList.addAll(settings)
        return newList
    }

    fun getCategories(): List<Category> {
        val newList = mutableListOf<Category>()
        newList.addAll(categories)
        return newList
    }

    fun getSettingByName(name: String): Optional<AbstractSetting<*>> {
        return Optional.ofNullable(settings.find { it.name == name })
    }

    fun getSettingsByCategory(category: String): List<AbstractSetting<*>> {
        return settings.filter { it.category == category }
    }

    fun getSettingsBySubCategory(category: String, subCategory: String): List<AbstractSetting<*>> {
        return settings.filter { it.category == category && it.subCategory == subCategory }
    }

    fun saveConfig() {
        if (!cfgFile.exists()) cfgFile.createNewFile()

        runCatching {
            cfgFile.bufferedWriter().use {
                val json = JsonObject()
                val settings = settings.sortedBy { it.category }
                for (setting in settings) {
                    val value: String = when (setting) {
                        is BooleanSetting -> setting.value.toString()
                        is KeybindSetting -> setting.value.toString()
                        is NumberSetting -> setting.value.toString()
                        is SelectorSetting -> setting.value.toString()
                        is TextSetting -> setting.value
                        else -> continue
                    }
                    json.addProperty("${setting.category}:${setting.subCategory}:${setting.name}", value)
                }
                it.write(gson.toJson(json))
            }
        }.onFailure {
            BladeCore.logger.error("Failed to save config ${cfgFile.name}!", it)
        }.onSuccess {
            BladeCore.logger.info("Successfully saved config ${cfgFile.name}!")
        }
    }

    fun loadConfig() {
        if (!cfgFile.exists()) return saveConfig()

        runCatching {
            val json = gson.fromJson(cfgFile.reader(), JsonObject::class.java)
            json.entrySet().forEach { (name, value) ->
                val settingName = name.split(":")
                getSetting(settingName[0], settingName[1], settingName[2]).ifPresent {
                    when (it) {
                        is BooleanSetting -> it.value = value.asString.lowercase().toBooleanStrictOrNull() ?: it.value
                        is KeybindSetting -> it.value = value.asString.toIntOrNull() ?: it.value
                        is NumberSetting -> it.value = value.asString.toDoubleOrNull() ?: it.value
                        is SelectorSetting -> it.value = value.asString.toIntOrNull() ?: it.value
                        is TextSetting -> it.value = value.asString ?: it.value
                    }
                    it.validate()
                }
            }
        }.onFailure {
            BladeCore.logger.error("Failed to load config ${cfgFile.name}!", it)
            saveConfig()
        }.onSuccess {
            BladeCore.logger.info("Successfully loaded config ${cfgFile.name}!")
        }
    }

    private fun getSetting(category: String, subCategory: String, name: String): Optional<AbstractSetting<*>> {
        return Optional.ofNullable(settings.find { it.category == category && it.subCategory == subCategory && it.name == name })
    }

    class Category(val name: String, val subCategories: PriorityQueue<SubCategory>)
    class SubCategory(val name: String, val settings: LinkedList<AbstractSetting<*>>)

    open class CategorySorting {
        open fun getCategoryComparator(): Comparator<in Category> = compareBy { it.name }
        open fun getSubcategoryComparator(): Comparator<in SubCategory> = compareBy { it.name }
    }
}