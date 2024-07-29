package com.mb.inventorymanagementservice.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.instrumentation.micrometer.v1_5.OpenTelemetryMeterRegistry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.Aggregation;
import io.opentelemetry.sdk.metrics.InstrumentType;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.AggregationTemporalitySelector;
import io.opentelemetry.sdk.metrics.export.DefaultAggregationSelector;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConditionalOnProperty(value = "management.newrelic.metrics.export.enabled", havingValue = "true")
public class MonitoringConfig {

    private static final String SERVICE_NAME_KEY = "service.name";
    private static final String INSTRUMENTATION_PROVIDER_KEY = "instrumentation.provider";
    private static final String INSTRUMENTATION_PROVIDER = "micrometer";
    private static final String API_KEY = "api-key";

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${management.newrelic.metrics.export.api-key}")
    private String apiKey;

    @Value("${management.newrelic.metrics.export.endpoint}")
    private String endpoint;

    @Bean
    public OpenTelemetry openTelemetry() {
        return OpenTelemetrySdk.builder()
                .setMeterProvider(
                        SdkMeterProvider.builder()
                                .setResource(
                                        Resource.getDefault().toBuilder()
                                                .put(SERVICE_NAME_KEY, applicationName)
                                                // Include instrumentation.provider=micrometer to enable micrometer metrics
                                                // experience in New Relic
                                                .put(INSTRUMENTATION_PROVIDER_KEY, INSTRUMENTATION_PROVIDER)
                                                .build())
                                .registerMetricReader(
                                        PeriodicMetricReader.builder(OtlpGrpcMetricExporter.builder()
                                                        .setEndpoint(endpoint)
                                                        .addHeader(API_KEY, apiKey)
                                                        // IMPORTANT: New Relic requires metrics to be delta temporality
                                                        .setAggregationTemporalitySelector(
                                                                AggregationTemporalitySelector.deltaPreferred())
                                                        // Use exponential histogram aggregation for histogram instruments
                                                        // to produce better data and compression
                                                        .setDefaultAggregationSelector(
                                                                DefaultAggregationSelector
                                                                        .getDefault()
                                                                        .with(InstrumentType.HISTOGRAM,
                                                                                Aggregation.base2ExponentialBucketHistogram()))
                                                        .build())
                                                // Match default micrometer collection interval of 60 seconds
                                                .setInterval(Duration.ofSeconds(5))
                                                .build())
                                .build())
                .build();
    }

    @Bean
    public MeterRegistry meterRegistry(OpenTelemetry openTelemetry) {
        return OpenTelemetryMeterRegistry.builder(openTelemetry).build();
    }
}
