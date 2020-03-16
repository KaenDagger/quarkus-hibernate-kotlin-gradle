package org.acme.fruitStore

import javax.persistence.*


@Entity
@Table(name = "known_fruits")
@NamedQuery(name = "Fruits.findAll", query = "SELECT f FROM Fruit f ORDER BY f.name", hints = [QueryHint(name = "org.hibernate.cacheable", value = "true")])
@Cacheable
class Fruit(){
    @Id
    @SequenceGenerator(name = "fruitsSequence", sequenceName = "known_fruits_id_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fruitsSequence")
    var id: Int? = null
    @Column(length = 40, unique = true)
    var name: String? = null

    constructor(name: String?) : this() {
        this.name = name
    }

}