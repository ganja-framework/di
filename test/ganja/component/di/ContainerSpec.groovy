package ganja.component.di

import spock.lang.Specification

class ContainerSpec extends Specification {

    void "it is initialisable"() {

        given:
        def container = new Container()

        expect:
        container instanceof Container
    }
}