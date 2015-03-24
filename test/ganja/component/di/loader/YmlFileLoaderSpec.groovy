package ganja.component.di.loader

import org.yaml.snakeyaml.Yaml
import spock.lang.Specification

class YmlFileLoaderSpec extends Specification {

    void "it can read yaml file"() {

        given:
        Yaml yaml = new Yaml()
        def subject = new YmlFileLoader(yaml: yaml)

        expect:
        println subject.load('/config/services.yml')
    }

    void "it throws exception if it cant find file"() {

        given:
        Yaml yaml = GroovyMock()
        def subject = new YmlFileLoader(yaml: yaml)

        when:
        subject.load('/non-existing.yml')

        then:
        FileNotFoundException exception = thrown()
        exception.getMessage() == 'File "/non-existing.yml" has not been found in resources folder'
    }
}
