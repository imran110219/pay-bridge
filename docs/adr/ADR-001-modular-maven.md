# ADR-001: Modular Maven architecture

Use a Maven reactor split into core, SPI, webhooks, providers, testkit, Spring adapter, and example. This protects framework/provider independence at the cost of more artifacts.
