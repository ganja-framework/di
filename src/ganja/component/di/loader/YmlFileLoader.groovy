package ganja.component.di.loader

import org.yaml.snakeyaml.Yaml

class YmlFileLoader implements FileLoaderInterface {

    Yaml yaml

    def load(String resource) {

        InputStream input = this.getClass().getResourceAsStream(resource)

        if(input) {
            return yaml.load(input)
        }
        else {
            throw new FileNotFoundException("File \"${resource}\" has not been found in resources folder")
        }
    }
}
