package utils

infix fun ClosedFloatingPointRange<Float>.intersects(other: ClosedFloatingPointRange<Float>): Boolean {
    return this.endInclusive >= other.start && other.endInclusive >= this.start
}