export type HabitHistory = { completedDates: string[]; missedDates: string[] };
export function changeHabitResult<T extends HabitHistory>(habit: T, date: string, failed: boolean): T {
  const key = failed ? "missedDates" : "completedDates";
  const other = failed ? "completedDates" : "missedDates";
  return { ...habit, [key]: habit[key].includes(date) ? habit[key].filter(d => d !== date) : [...habit[key], date], [other]: habit[other].filter(d => d !== date) };
}
export function calendarState(date: string, today: string, hasEntries: boolean, complete: boolean, failed: boolean) {
  return date > today ? "future" : !hasEntries ? "empty" : complete ? "complete" : failed || date < today ? "missed" : "empty";
}
export function clampDial(value: number, min: number, max: number) { return Math.max(min, Math.min(max, Math.round(value))); }
export function angleDelta(next: number, previous: number) {
  let delta = next - previous;
  if (delta > 180) delta -= 360;
  if (delta < -180) delta += 360;
  return delta;
}
export function scopeFlag(scope: "instant" | "limits" | "windows") { return scope === "limits" ? "limitsEnabled" : scope === "windows" ? "windowsEnabled" : "enabled"; }

export function deterministicIndex(date: string, length: number, salt = "mach-daily-quest") {
  if (length <= 0) return -1;
  let hash = 2166136261;
  for (const character of `${salt}:${date}`) {
    hash ^= character.charCodeAt(0);
    hash = Math.imul(hash, 16777619);
  }
  return (hash >>> 0) % length;
}

export function streakFromDates(completedDates: string[], today: string) {
  const completed = new Set(completedDates);
  const cursor = new Date(`${today}T12:00:00`);
  let streak = 0;
  while (completed.has(`${cursor.getFullYear()}-${String(cursor.getMonth() + 1).padStart(2, "0")}-${String(cursor.getDate()).padStart(2, "0")}`)) {
    streak++;
    cursor.setDate(cursor.getDate() - 1);
  }
  return streak;
}

export function rewardProgress(previousXp: number, earnedXp: number, interval: number, rewardMinutes: number, availableMinutes: number, cap: number) {
  const safeInterval = Math.max(1, interval);
  const nextXp = Math.max(0, previousXp) + Math.max(0, earnedXp);
  const milestones = Math.floor(nextXp / safeInterval) - Math.floor(Math.max(0, previousXp) / safeInterval);
  return {
    xp: nextXp,
    earnedMinutes: Math.max(0, milestones) * Math.max(0, rewardMinutes),
    availableMinutes: Math.min(Math.max(0, cap), Math.max(0, availableMinutes) + Math.max(0, milestones) * Math.max(0, rewardMinutes)),
  };
}
