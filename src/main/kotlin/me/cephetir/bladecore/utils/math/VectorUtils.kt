package me.cephetir.bladecore.utils.math

import net.minecraft.entity.Entity
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import net.minecraft.world.ChunkCoordIntPair
import kotlin.math.*

object VectorUtils {

    /**
     * Get all block positions inside a 3d area between pos1 and pos2
     *
     * @param pos1 Starting blockPos
     * @param pos2 Ending blockPos
     * @return block positions inside a 3d area between pos1 and pos2
     */
    fun getBlockPositionsInArea(pos1: BlockPos, pos2: BlockPos): List<BlockPos> {
        val minX = min(pos1.x, pos2.x)
        val maxX = max(pos1.x, pos2.x)
        val minY = min(pos1.y, pos2.y)
        val maxY = max(pos1.y, pos2.y)
        val minZ = min(pos1.z, pos2.z)
        val maxZ = max(pos1.z, pos2.z)
        return getBlockPos(minX, maxX, minY, maxY, minZ, maxZ)
    }

    private fun getBlockPos(minX: Int, maxX: Int, minY: Int, maxY: Int, minZ: Int, maxZ: Int): List<BlockPos> {
        val returnList = ArrayList<BlockPos>()
        for (x in minX..maxX)
            for (z in minZ..maxZ)
                for (y in minY..maxY)
                    returnList.add(BlockPos(x, y, z))
        return returnList
    }

    /**
     * Get all block positions inside a sphere with given [radius]
     *
     * @param center Center of the sphere
     * @param radius Radius of the sphere
     * @return block positions inside a sphere with given [radius]
     */
    fun getBlockPosInSphere(center: Vec3d, radius: Float): ArrayList<BlockPos> {
        val squaredRadius = radius.pow(2)
        val posList = ArrayList<BlockPos>()
        for (x in getAxisRange(center.x, radius)) for (y in getAxisRange(center.y, radius)) for (z in getAxisRange(center.z, radius)) {
            /* Valid position check */
            val blockPos = BlockPos(x, y, z)
            if (blockPos.distanceSqToCenter(center.x, center.y, center.z) > squaredRadius) continue
            posList.add(blockPos)
        }
        return posList
    }

    private fun getAxisRange(d1: Double, d2: Float): IntRange = IntRange(floor(d1 - d2).toInt(), ceil(d1 + d2).toInt())

    fun Vec3d.toBlockPos(): BlockPos = BlockPos(floor(x).toInt(), floor(y).toInt(), floor(z).toInt())

    fun Vec3i.toVec3d(): Vec3d = toVec3d(0.0, 0.0, 0.0)

    fun Vec3i.toVec3d(offSet: Vec3d): Vec3d = Vec3d(x + offSet.x, y + offSet.y, z + offSet.z)

    fun Vec3i.toVec3d(xOffset: Double, yOffset: Double, zOffset: Double): Vec3d = Vec3d(x + xOffset, y + yOffset, z + zOffset)

    fun Vec3i.toVec3dCenter(): Vec3d = toVec3dCenter(0.0, 0.0, 0.0)

    fun Vec3i.toVec3dCenter(offSet: Vec3d): Vec3d = Vec3d(x + 0.5 + offSet.x, y + 0.5 + offSet.y, z + 0.5 + offSet.z)

    fun Vec3i.toVec3dCenter(xOffset: Double, yOffset: Double, zOffset: Double): Vec3d = Vec3d(x + 0.5 + xOffset, y + 0.5 + yOffset, z + 0.5 + zOffset)

    fun Vec3i.distanceTo(vec3i: Vec3i): Double {
        val xDiff = vec3i.x - x
        val yDiff = vec3i.y - y
        val zDiff = vec3i.z - z
        return sqrt((xDiff * xDiff + yDiff * yDiff + zDiff * zDiff).toDouble())
    }

    fun Vec3d.distanceTo(vec3i: Vec3i): Double {
        val xDiff = vec3i.x + 0.5 - x
        val yDiff = vec3i.y + 0.5 - y
        val zDiff = vec3i.z + 0.5 - z
        return sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff)
    }

    fun Entity.distanceTo(vec3i: Vec3i): Double {
        val xDiff = vec3i.x + 0.5 - posX
        val yDiff = vec3i.y + 0.5 - posY
        val zDiff = vec3i.z + 0.5 - posZ
        return sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff)
    }

    fun Entity.distanceTo(Vec3d: Vec3d): Double {
        val xDiff = Vec3d.x - posX
        val yDiff = Vec3d.y - posY
        val zDiff = Vec3d.z - posZ
        return sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff)
    }

    fun Entity.distanceTo(chunkPos: ChunkCoordIntPair): Double {
        return hypot(chunkPos.chunkXPos * 16 + 8 - posX, chunkPos.chunkZPos * 16 + 8 - posZ)
    }

    fun Vec3i.multiply(multiplier: Int): Vec3i = Vec3i(x * multiplier, y * multiplier, z * multiplier)

    infix operator fun Vec3d.times(Vec3d: Vec3d): Vec3d = Vec3d(x * Vec3d.x, y * Vec3d.y, z * Vec3d.z)

    infix operator fun Vec3d.times(multiplier: Double): Vec3d = Vec3d(x * multiplier, y * multiplier, z * multiplier)

    infix operator fun Vec3.times(Vec3: Vec3): Vec3 = Vec3(xCoord * Vec3.xCoord, yCoord * Vec3.yCoord, zCoord * Vec3.zCoord)

    infix operator fun Vec3.times(multiplier: Double): Vec3 = Vec3(xCoord * multiplier, yCoord * multiplier, zCoord * multiplier)

    infix operator fun Vec3d.plus(Vec3d: Vec3d): Vec3d = add(Vec3d)

    infix operator fun Vec3d.minus(Vec3d: Vec3d): Vec3d = subtract(Vec3d)
}