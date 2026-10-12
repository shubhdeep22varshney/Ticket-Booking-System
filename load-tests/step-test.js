import http from 'k6/http';
import { check } from 'k6';

// ================= CONFIG =================
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
// ==========================================

// Each step runs one fixed request rate for 30 seconds, one after another.
// Results are reported separately per step (tagged by scenario name).
const RATES = [50, 100, 150, 200];
const STEP_SECONDS = 30;

const scenarios = {};
RATES.forEach((rate, i) => {
  scenarios[`rate_${rate}`] = {
    executor: 'constant-arrival-rate',
    rate: rate,
    timeUnit: '1s',
    duration: `${STEP_SECONDS}s`,
    startTime: `${i * (STEP_SECONDS + 5)}s`,   // 5s pause between steps
    preAllocatedVUs: 200,
    maxVUs: 1000,                              // enough VUs so k6 never runs out
  };
});

const thresholds = {};
RATES.forEach((rate) => {
  thresholds[`http_req_duration{scenario:rate_${rate}}`] = ['p(95)<800'];
  thresholds[`http_req_failed{scenario:rate_${rate}}`] = ['rate<0.01'];
  thresholds[`dropped_iterations{scenario:rate_${rate}}`] = ['count==0'];
});

export const options = { scenarios, thresholds };

export default function () {
  const res = http.get(`${BASE_URL}/api/events`);
  check(res, { 'status 200': (r) => r.status === 200 });
}