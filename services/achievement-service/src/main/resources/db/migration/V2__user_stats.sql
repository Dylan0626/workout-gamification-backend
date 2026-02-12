CREATE TABLE user_stats (
  user_id TEXT PRIMARY KEY,
  workouts_count INT NOT NULL,
  calories_total INT NOT NULL,
  last_workout_date DATE,
  streak_days INT NOT NULL
);
