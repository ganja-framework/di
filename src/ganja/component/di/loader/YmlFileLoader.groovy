package ganja.component.di.loader

import org.yaml.snakeyaml.Yaml

class YmlFileLoader implements FileLoaderInterface {

    def logger

    Yaml yaml

    String prefix

    def load(String resource) {

        if(prefix) {
            resource = sprintf('/%s/%s', [ prefix.replaceAll('[^A-Za-z]',''), resource.replaceAll('^/*','')])
        }

        logger?.info("Attempting to load '${resource}' file")

        InputStream input = this.getClass().getResourceAsStream(resource)

        if(input) {
            return yaml.load(input)
        }
        else {
            throw new FileNotFoundException("File \"${resource}\" has not been found in resources folder")
        }
    }
}
