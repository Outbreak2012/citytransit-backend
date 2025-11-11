-- Tabla para análisis de transacciones en ClickHouse
CREATE TABLE IF NOT EXISTS transaction_records (
    transactionId UInt64,
    cardId UInt64,
    transactionType String,
    amount Decimal(10, 2),
    transactionTime DateTime,
    vehicleId String,
    routeId String
) ENGINE = MergeTree()
ORDER BY (transactionTime, transactionId)
PARTITION BY toYYYYMM(transactionTime);

-- Índices para mejorar performance
CREATE INDEX IF NOT EXISTS idx_card ON transaction_records (cardId) TYPE bloom_filter GRANULARITY 1;
CREATE INDEX IF NOT EXISTS idx_route ON transaction_records (routeId) TYPE bloom_filter GRANULARITY 1;
