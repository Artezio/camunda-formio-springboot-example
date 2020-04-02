package com.artezio.forms.resources;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public class ClassPathResourceLoader implements ResourceLoader {

    private static final String ROOT_DIRECTORY = "public";
    private static final ClassLoader CLASS_LOADER = ClassPathResourceLoader.class.getClassLoader();

    @Override
    public InputStream getResource(String resourceKey) {
        resourceKey = String.format("%s/%s", ROOT_DIRECTORY, resourceKey);
        return CLASS_LOADER.getResourceAsStream(resourceKey);
    }

    @Override
    public List<String> listResourceNames() {
        return listResourceNames(ROOT_DIRECTORY).stream()
                .map(resourceName -> resourceName.substring((ROOT_DIRECTORY + "/").length()))
                .collect(Collectors.toList());
    }

    private List<String> listResourceNames(String resourcesDirectory) {
        try {
            URL url = CLASS_LOADER.getResource(resourcesDirectory);
            if (url == null) return emptyList();
            File resource = new File(url.toURI());
            return Arrays.stream(resource.listFiles())
                    .flatMap(file -> {
                        String resourceName = String.format("%s/%s", resourcesDirectory, file.getName());
                        return file.isDirectory()
                                ? listResourceNames(resourceName).stream()
                                : Stream.of(resourceName);
                    })
                    .collect(Collectors.toList());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
