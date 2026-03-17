package com.example

import com.example.processor.InspectProperties

interface HasName {
    val name: String
}

abstract class BaseEntity {
    @Column(name = "name") lateinit var name: String
}

// ISSUE: extends BaseEntity (has @Column) + also implements HasName (no annotation)
// getAllProperties() "loses" the @Column annotation
@InspectProperties
class WithInterface : BaseEntity(), HasName

// CONTROL: extends BaseEntity only, no conflicting interface
// getAllProperties() correctly finds @Column
@InspectProperties
class Control : BaseEntity()