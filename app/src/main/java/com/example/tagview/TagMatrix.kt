package com.example.tagview

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * @author kun
 * @since 5/13/21
 */
data class TagPoint(var x: Double = 0.0, var y: Double = 0.0, var z: Double = 0.0)
class TagMatrix(val column: Int, val row: Int, val matrix: Array<DoubleArray>)

fun tagMatrixMake(column: Int, row: Int): TagMatrix {
    val matrix = Array(column) { DoubleArray(row) { 0.0 } }
    return TagMatrix(column, row, matrix)
}

fun tagMatrixMakeFromArray(column: Int, row: Int, data: Array<DoubleArray>): TagMatrix {
    val tagMatrix = tagMatrixMake(column, row)
    for (i in 0 until column) {
        for (j in 0 until row) {
            tagMatrix.matrix[i][j] = data[i][j]
        }
    }
    return tagMatrix
}

fun tagMatrixMultiply(m1: TagMatrix, m2: TagMatrix): TagMatrix {
    val result = tagMatrixMake(m1.column, m2.row)
    for (i in 0 until m1.column) {
        for (j in 0 until m2.row) {
            for (k in 0 until m1.row) {
                result.matrix[i][j] += m1.matrix[i][k] * m2.matrix[k][j]
            }
        }
    }
    return result
}

fun TagPoint.pointRotation(direction: TagPoint, angle: Double): TagPoint {
    if (angle == 0.0) return this
    val temp = Array(1) { doubleArrayOf(x, y, z, 1.0) }
    var result = tagMatrixMakeFromArray(1, 4, temp)

    val distanceYZ = direction.z * direction.z + direction.y * direction.y
    if (distanceYZ != 0.0) {
        val cos1 = direction.z / sqrt(distanceYZ)
        val sin1 = direction.y / sqrt(distanceYZ)
        val t1 = Array(4) { DoubleArray(4) }.apply {
            set(0, doubleArrayOf(1.0, 0.0, 0.0, 0.0))
            set(1, doubleArrayOf(0.0, cos1, sin1, 0.0))
            set(2, doubleArrayOf(0.0, -sin1, cos1, 0.0))
            set(3, doubleArrayOf(0.0, 0.0, 0.0, 1.0))
        }
        val m1 = tagMatrixMakeFromArray(4, 4, t1)
        result = tagMatrixMultiply(result, m1)
    }

    val distanceXYZ =
        direction.x * direction.x + direction.y * direction.y + direction.z * direction.z
    if (distanceXYZ != 0.0) {
        val cos2 = sqrt(distanceYZ) / sqrt(distanceXYZ)
        val sin2 = -direction.x / sqrt(distanceXYZ)
        val t2 = Array(4) { DoubleArray(4) }.apply {
            set(0, doubleArrayOf(cos2, 0.0, -sin2, 0.0))
            set(1, doubleArrayOf(0.0, 1.0, 0.0, 0.0))
            set(2, doubleArrayOf(sin2, 0.0, cos2, 0.0))
            set(3, doubleArrayOf(0.0, 0.0, 0.0, 1.0))
        }
        val m2 = tagMatrixMakeFromArray(4, 4, t2)
        result = tagMatrixMultiply(result, m2)
    }

    val cos3 = cos(angle)
    val sin3 = sin(angle)
    val t3 = Array(4) { DoubleArray(4) }.apply {
        set(0, doubleArrayOf(cos3, sin3, 0.0, 0.0))
        set(1, doubleArrayOf(-sin3, cos3, 0.0, 0.0))
        set(2, doubleArrayOf(0.0, 0.0, 1.0, 0.0))
        set(3, doubleArrayOf(0.0, 0.0, 0.0, 1.0))
    }
    val m3 = tagMatrixMakeFromArray(4, 4, t3)
    result = tagMatrixMultiply(result, m3)

    if (distanceXYZ != 0.0) {
        val cos2 = sqrt(distanceYZ) / sqrt(distanceXYZ)
        val sin2 = -direction.x / sqrt(distanceXYZ)
        val t2 = Array(4) { DoubleArray(4) }.apply {
            set(0, doubleArrayOf(cos2, 0.0, sin2, 0.0))
            set(1, doubleArrayOf(0.0, 1.0, 0.0, 0.0))
            set(2, doubleArrayOf(-sin2, 0.0, cos2, 0.0))
            set(3, doubleArrayOf(0.0, 0.0, 0.0, 1.0))
        }
        val m2 = tagMatrixMakeFromArray(4, 4, t2)
        result = tagMatrixMultiply(result, m2)
    }

    if (distanceYZ != 0.0) {
        val cos1 = direction.z / sqrt(distanceYZ)
        val sin1 = direction.y / sqrt(distanceYZ)
        val t1 = Array(4) { DoubleArray(4) }.apply {
            set(0, doubleArrayOf(1.0, 0.0, 0.0, 0.0))
            set(1, doubleArrayOf(0.0, cos1, -sin1, 0.0))
            set(2, doubleArrayOf(0.0, sin1, cos1, 0.0))
            set(3, doubleArrayOf(0.0, 0.0, 0.0, 1.0))
        }
        val m1 = tagMatrixMakeFromArray(4, 4, t1)
        result = tagMatrixMultiply(result, m1)
    }

    return TagPoint(result.matrix[0][0], result.matrix[0][1], result.matrix[0][2])
}