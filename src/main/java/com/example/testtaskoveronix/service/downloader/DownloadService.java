package com.example.testtaskoveronix.service.downloader;

import java.util.Set;
import com.example.testtaskoveronix.model.Source;

public interface DownloadService<R, T> {
    Set<T> download(String url, Class<? extends R> clazz, String apiKey, Source source, Boolean isActual);
}
