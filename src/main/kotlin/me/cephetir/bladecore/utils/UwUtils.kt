package me.cephetir.bladecore.utils

import me.cephetir.bladecore.core.cache.impl.MapCache
import java.util.regex.Matcher
import java.util.regex.Pattern

object UwUtils {

    private val kaomoji = """
                    (* ^ ω ^)
                    (´ ∀ ` *)
                    ٩(◕‿◕｡)۶
                    ☆*:.｡.o(≧▽≦)o.｡.:*☆
                    (o^▽^o)
                    (⌒▽⌒)☆
                    <(￣︶￣)>
                    。.:☆*:･'(*⌒―⌒*)))
                    ヽ(・∀・)ﾉ
                    (´｡• ω •｡`)
                    (￣ω￣)
                    ｀;:゛;｀;･(°ε° )
                    (o･ω･o)
                    (＠＾◡＾)
                    ヽ(*・ω・)ﾉ
                    (o_ _)ﾉ彡☆
                    (^人^)
                    (o´▽`o)
                    (*´▽`*)
                    ｡ﾟ( ﾟ^∀^ﾟ)ﾟ｡
                    ( ´ ω ` )
                    (((o(*°▽°*)o)))
                    (≧◡≦)
                    (o´∀`o)
                    (´• ω •`)
                    (＾▽＾)
                    (⌒ω⌒)
                    ∑d(°∀°d)
                    ╰(▔∀▔)╯
                    (─‿‿─)
                    (*^‿^*)
                    ヽ(o^ ^o)ﾉ
                    (✯◡✯)
                    (◕‿◕)
                    (*≧ω≦*)
                    (☆▽☆)
                    (⌒‿⌒)
                    ＼(≧▽≦)／
                    ヽ(o＾▽＾o)ノ
                    ☆ ～('▽^人)
                    (*°▽°*)
                    ٩(｡•́‿•̀｡)۶
                    (✧ω✧)
                    ヽ(*⌒▽⌒*)ﾉ
                    (´｡• ᵕ •｡`)
                    ( ´ ▽ ` )
                    (￣▽￣)
                    ╰(*´︶`*)╯
                    ヽ(>∀<☆)ノ
                    o(≧▽≦)o
                    (☆ω☆)
                    (っ˘ω˘ς )
                    ＼(￣▽￣)／
                    (*¯︶¯*)
                    ＼(＾▽＾)／
                    ٩(◕‿◕)۶
                    (o˘◡˘o)
                    \\(★ω★)/
                    \\(^ヮ^)/
                    (〃＾▽＾〃)
                    (╯✧▽✧)╯
                    o(>ω<)o
                    o( ❛ᴗ❛ )o
                    ｡ﾟ(TヮT)ﾟ｡
                    ( ‾́ ◡ ‾́ )
                    (ﾉ´ヮ`)ﾉ*: ･ﾟ
                    (b ᵔ▽ᵔ)b
                    (๑˃ᴗ˂)ﻭ
                    (๑˘︶˘๑)
                    ( ˙꒳˙ )
                    (*꒦ິ꒳꒦ີ)
                    °˖✧◝(⁰▿⁰)◜✧˖°
                    (´･ᴗ･ ` )
                    (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧
                    („• ֊ •„)
                    (.❛ ᴗ ❛.)
                    (⁀ᗢ⁀)
                    (￢‿￢ )
                    (¬‿¬ )
                    (*￣▽￣)b
                    ( ˙▿˙ )
                    (¯▿¯)
                    ( ◕▿◕ )
                    ＼(٥⁀▽⁀ )／
                    („• ᴗ •„)
                    (ᵔ◡ᵔ)
                    ( ´ ▿ ` )
                    (づ￣ ³￣)づ
                    (つ≧▽≦)つ
                    (つ✧ω✧)つ
                    (づ ◕‿◕ )づ
                    (⊃｡•́‿•̀｡)⊃
                    (つ . •́ _ʖ •̀ .)つ
                    (っಠ‿ಠ)っ
                    (づ◡﹏◡)づ
                    ⊂(´• ω •`⊂)
                    ⊂(･ω･*⊂)
                    ⊂(￣▽￣)⊃
                    ⊂( ´ ▽ ` )⊃
                    ( ~*-*)~
                    (^_~)
                    ( ﾟｏ⌒)
                    (^_-)≡☆
                    (^ω~)
                    (>ω^)
                    (~人^)
                    (^_-)
                    ( -_・)
                    (^_<)〜☆
                    (^人<)〜☆
                    ☆⌒(≧▽° )
                    ☆⌒(ゝ。∂)
                    (^_<)
                    (^_−)☆
                    (･ω<)☆
                    (^.~)☆
                    (^.~)
                    (ﾉ´ з `)ノ
                    (♡μ_μ)
                    (*^^*)♡
                    ☆⌒ヽ(*'､^*)chu
                    (♡-_-♡)
                    (￣ε￣＠)
                    ヽ(♡‿♡)ノ
                    ( ´ ∀ `)ノ～ ♡
                    (─‿‿─)♡
                    (´｡• ᵕ •｡`) ♡
                    (*♡∀♡)
                    (｡・//ε//・｡)
                    (´ ω `♡)
                    ♡( ◡‿◡ )
                    (◕‿◕)♡
                    (/▽＼*)｡o○♡
                    (ღ˘⌣˘ღ)
                    (♡°▽°♡)
                    ♡(｡- ω -)
                    ♡ ～('▽^人)
                    (´• ω •`) ♡
                    (´ ε ` )♡
                    (´｡• ω •｡`) ♡
                    ( ´ ▽ ` ).｡ｏ♡
                    ╰(*´︶`*)╯♡
                    (*˘︶˘*).｡.:*♡
                    (♡˙︶˙♡)
                    ♡＼(￣▽￣)／♡
                    (≧◡≦) ♡
                    (⌒▽⌒)♡
                    (*¯ ³¯*)♡
                    (っ˘з(˘⌣˘ ) ♡
                    ♡ (˘▽˘>ԅ( ˘⌣˘)
                    ( ˘⌣˘)♡(˘⌣˘ )
                    (/^-^(^ ^*)/ ♡
                    ٩(♡ε♡)۶
                    σ(≧ε≦σ) ♡
                    ♡ (⇀ 3 ↼)
                    ♡ (￣З￣)
                    (´♡‿♡`)
                    (°◡°♡)
                    Σ>―(〃°ω°〃)♡→
                    (´,,•ω•,,)♡
                    (´꒳`)♡
    """.trimIndent().split("\n").dropLastWhile { it.isEmpty() }.toTypedArray()

    private val matchers: HashMap<Matcher, String> = object : HashMap<Matcher, String>() {
        init {
            put(Pattern.compile("(?<!\u00A7)[lr]").matcher(""), "w")
            put(Pattern.compile("[LR]").matcher(""), "W")
            put(Pattern.compile("(?<![§\\\\oO])n([aeiou])").matcher(""), "ny$1")
            put(Pattern.compile("N([aeiou])").matcher(""), "Ny$1")
            put(Pattern.compile("N([AEIOU])").matcher(""), "NY$1")
        }
    }
    private val punctuations = listOf(
        Pattern.compile("!( |$)").matcher(""),
        Pattern.compile("\\.( |$)").matcher(""),
        Pattern.compile(",( |$)").matcher("")
    )

    /**
     * Prepends a randomly chosen Kaomoji to the given String
     *
     * @param string The string the process
     * @return `string` with a random Kaomoji prepended
     */
    fun uwuify(string: String): String {
        if (textCache.isCached(string))
            return textCache.get(string)!!

        var text = string
            .replace("th", "d")
            .replace("Th", "D")
            .replace(" is", " ish")
            .replace(" Is", " Ish")
            .replace("ove", "uv")

        for (match in matchers.entries)
            text = match.key.reset(text).replaceAll(match.value)
        for (punctuation in punctuations)
            text = punctuation.reset(text).replaceAll(" " + kaomoji[string.length % kaomoji.size] + " ")

        textCache.cache(string, text)
        return text
    }

    /**
     * @return A random Kaomoji
     */
    fun uwuGen(): String = kaomoji[(Math.random() * (kaomoji.size - 1)).toInt()]

    val textCache = MapCache<String, String>(25000)
}