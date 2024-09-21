package me.netkas.lifelesslife.objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public final class ResourceDefinition {
    private final String folder;
    private final String defaultLocale;
    private final List<String> locales;

    @JsonCreator
    public ResourceDefinition(
            @JsonProperty("folder") String folder,
            @JsonProperty("default_locale") String defaultLocale,
            @JsonProperty("locales") List<String> locales) {
        this.folder = folder;
        this.defaultLocale = defaultLocale;
        this.locales = locales;
    }

    public String getFolder() {
        return folder;
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public List<String> getLocales() {
        return locales;
    }
}