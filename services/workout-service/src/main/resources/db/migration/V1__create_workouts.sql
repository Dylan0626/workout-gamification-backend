CREATE TABLE workouts (
  workout_id UUID PRIMARY KEY,
  user_id TEXT NOT NULL,
  workout_type TEXT NOT NULL,
  duration_minutes INT NOT NULL,
  calories_burned INT NOT NULL,
  workout_ts TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_workouts_user_id ON workouts(user_id);
CREATE INDEX idx_workouts_user_ts ON workouts(user_id, workout_ts DESC);
