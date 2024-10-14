package com.example.notescompleted

class Note(val heading: String, val description: String) {
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (this === other) return true
        if (other !is Note) return false
        if (heading != other.heading || description != other.description) return false
        return true
    }

    override fun hashCode(): Int = heading.hashCode() * 31 + description.hashCode() * 31
}