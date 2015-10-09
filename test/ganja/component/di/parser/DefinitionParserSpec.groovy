package ganja.component.di.parser

import ganja.common.di.ContainerInterface
import ganja.common.di.DefinitionInterface
import ganja.component.di.Parameter
import spock.lang.Specification

class DefinitionParserSpec extends Specification {

    void 'it is initialisable'() {

        given:
        def subject = new DefinitionParser()

        expect:
        subject instanceof DefinitionParser
    }

    void 'it can parse parameters'() {

        given:
        def subject = new DefinitionParser()
        ContainerInterface container = Mock()

        when:
        subject.parse([ parameters: [ param1: 'some value']], container)

        then:
        1 * container.setParameter('param1', 'some value')
    }

    void 'it can parse simple services'() {

        given:
        def subject = new DefinitionParser()
        ContainerInterface container = Mock()

        when:
        subject.parse([ services: [ service1: [ class: 'java.util.Date' ]]], container)

        then:
        1 * container.register('service1', _)
    }

    void 'it can parse service with map arguments'() {

        given:
        def subject = new DefinitionParser()
        ContainerInterface container = Mock()
        DefinitionInterface definition = Mock()

        when:
        subject.parse(
            [ services: [
                service1: [ class: 'support.Service', arguments: [ name: 'test']]
            ]],
            container
        )

        then:
        1 * container.register('service1', _) >> definition
        1 * definition.setArguments([ name: 'test'])
    }

    void 'it can parse service with list arguments'() {

        given:
        def subject = new DefinitionParser()
        ContainerInterface container = Mock()
        DefinitionInterface definition = Mock()

        when:
        subject.parse(
            [ services: [
                service1: [ class: 'support.ServiceWithConstructor', arguments: [ 'test' ]]
            ]],
            container
        )

        then:
        1 * container.register('service1', _) >> definition
        1 * definition.setArguments([ 'test' ])
    }

    void 'it can parse service with list arguments and params references'() {

        given:
        def subject = new DefinitionParser()
        ContainerInterface container = Mock()
        DefinitionInterface definition1 = Mock()
        DefinitionInterface definition2 = Mock()

        when:
        subject.parse(
            [ services: [
                service1: [ class: 'support.ServiceWithConstructor', arguments: [ '.test' ]],
                service2: [ class: 'support.Service', arguments: [ name: '.test' ]]
            ]],
            container
        )

        then:
        1 * container.register('service1', _) >> definition1
        1 * container.register('service2', _) >> definition2
        1 * definition1.setArguments(_) >> {
            assert it[0][0] instanceof Parameter
        }
        1 * definition2.setArguments(_) >> {
            assert it[0]['name'] instanceof Parameter
        }
    }

    void 'it can parse service with method calls'() {

        given:
        def subject = new DefinitionParser()
        ContainerInterface container = Mock()
        DefinitionInterface definition = Mock()

        when:
        subject.parse(
            [ services: [
                service1: [ class: 'support.Service', calls: [
                    [ method: 'setName', argumetns: 'test' ],
                    [ method: 'getName' ]
                ]]
            ]],
            container
        )

        then:
        1 * container.register('service1', _) >> definition
        2 * definition.calls(_,_)
    }

    void 'it can parse service with tags'() {

        given:
        def subject = new DefinitionParser()
        ContainerInterface container = Mock()
        DefinitionInterface definition = Mock()

        when:
        subject.parse(
            [ services: [
                service1: [ class: 'support.Service', tags: [[ name: 'test', key: 'value' ]]]
            ]],
            container
        )

        then:
        1 * container.register('service1', _) >> definition
        1 * definition.setTags([[ name: 'test', key: 'value' ]])
    }
}
