CREATE TABLE achievements (
  achievement_id UUID PRIMARY KEY,
  user_id TEXT NOT NULL,
  code TEXT NOT NULL,
  title TEXT NOT NULL,
  awarded_at TIMESTAMPTZ NOT NULL
);

-- user_id + code should be unique (prevents awarding same achievement twice)
CREATE UNIQUE INDEX uq_user_code ON achievements(user_id, code);
CREATE INDEX idx_achievements_user ON achievements(user_id);

-- Idempotency: track processed Kafka events
CREATE TABLE processed_events (
  event_id UUID PRIMARY KEY,
  processed_at TIMESTAMPTZ NOT NULL
);
