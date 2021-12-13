package me.bardy.ticket

import java.util.function.Predicate
import kotlin.math.max
import kotlin.math.min

@Suppress("UNCHECKED_CAST", "EqualsOrHashCode")
class SortedArraySet<T>(
    private val comparator: Comparator<T>,
    initialCapacity: Int = DEFAULT_INITIAL_CAPACITY,
) : AbstractMutableSet<T>() {

    private var contents = arrayOfNulls<Any>(initialCapacity) as Array<T?>
    override var size = 0

    val first: T?
        get() = contents[0]
    val last: T?
        get() = contents[size - 1]

    fun addOrGet(element: T): T? {
        val index = findIndex(element)
        if (index >= 0) return contents[index]
        addInternal(element, -index - 1)
        return element
    }

    fun get(element: T): T? {
        val index = findIndex(element)
        return if (index >= 0) contents[index] else null
    }

    override fun add(element: T): Boolean {
        val index = findIndex(element)
        if (index >= 0) return false
        addInternal(element, -index - 1)
        return true
    }

    override fun remove(element: T): Boolean {
        val index = findIndex(element)
        return if (index >= 0) {
            removeInternal(index)
            true
        } else false
    }

    override fun contains(element: T) = findIndex(element) >= 0

    override fun iterator(): MutableIterator<T> = ArrayIterator()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SortedArraySet<Any>
        if (comparator == other.comparator) return size == other.size && contents.contentEquals(other.contents)
        return super.equals(other)
    }

    override fun toArray() = contents.clone()

    override fun <U> toArray(a: Array<U?>): Array<U?> {
        if (a.size < size) return contents.copyOf(size) as Array<U?>
        System.arraycopy(contents, 0, a, 0, size)
        if (a.size > size) a[size] = null
        return a
    }

    override fun clear() {
        contents.fill(null, 0, size)
        size = 0
    }

    override fun removeIf(filter: Predicate<in T>): Boolean {
        var i = 0
        val len = size
        val backing = contents
        while (true) {
            if (i >= len) return false
            if (!filter.test(backing[i]!!)) {
                ++i
                continue
            }
            break
        }
        var lastIndex = i
        while (i < len) {
            val curr = backing[i]!!
            if (!filter.test(curr)) backing[lastIndex++] = curr
            ++i
        }
        backing.fill(null, lastIndex, len)
        size = lastIndex
        return true
    }

    private fun findIndex(element: T) = contents.binarySearch(element, comparator as Comparator<T?>, 0, size)

    private fun addInternal(element: T, index: Int) {
        grow(size + 1)
        if (index != size) System.arraycopy(contents, index, contents, index + 1, size - index)
        contents[index] = element
        ++size
    }

    private fun removeInternal(index: Int) {
        --size
        if (index != size) System.arraycopy(contents, index + 1, contents, index, size - index)
        contents[size] = null
    }

    private fun grow(minCapacity: Int) {
        if (minCapacity <= contents.size) return
        val capacity = if (contents.isEmpty()) max(min(contents.size.toLong() + (contents.size shr 1).toLong(), 2147483639L), minCapacity.toLong()).toInt() else if (minCapacity < 10) 10 else minCapacity
        val newArray = arrayOfNulls<Any>(capacity)
        System.arraycopy(contents, 0, newArray, 0, size)
        contents = newArray as Array<T?>
    }

    private inner class ArrayIterator : MutableIterator<T> {

        private var index = 0
        private var last = -1

        override fun hasNext() = index < size

        override fun next(): T = if (index >= size) {
            throw NoSuchElementException()
        } else {
            last = index++
            contents[last]!!
        }

        override fun remove() {
            check(last != -1)
            removeInternal(last)
            --index
            last = -1
        }
    }

    companion object {

        private const val DEFAULT_INITIAL_CAPACITY = 10

        fun <T : Comparable<T>> create(initialCapacity: Int = DEFAULT_INITIAL_CAPACITY) = SortedArraySet(naturalOrder<T>(), initialCapacity)
    }
}
