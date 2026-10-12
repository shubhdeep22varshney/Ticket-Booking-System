import http from 'k6/http';
import exec from 'k6/execution';
import { check, sleep } from 'k6';

// ================= CONFIG (ADAPT these to your project) =================
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';

const ADMIN = { email: 'admin@gmail.com', password: 'admin123' };   // an existing ADMIN account
const USER  = { fullName: 'Load User', email: 'loaduser@test.com', password: 'user123' };

const TOTAL_SEATS = 300;   // must be >= booking VUs x iterations (20 x 10 = 200)
// ========================================================================

export const options = {
  scenarios: {
    // Many users browsing events (read-heavy traffic)
    browse: {
      executor: 'ramping-vus',
      exec: 'browse',
      stages: [
        { duration: '20s', target: 50 },
        { duration: '40s', target: 50 },
        { duration: '10s', target: 0 },
      ],
    },
    // Users booking DIFFERENT seats (measures booking throughput, no conflicts)
    booking: {
      executor: 'per-vu-iterations',
      exec: 'book',
      vus: 20,
      iterations: 10,
      startTime: '5s',
      maxDuration: '2m',
    },
  },
  thresholds: {
    http_req_failed: ['rate<0.01'],      // under 1% errors
    http_req_duration: ['p(95)<800'],    // 95% of requests under 800 ms
  },
};

const json = { headers: { 'Content-Type': 'application/json' } };


function login(creds) {
  const res = http.post(
    `${BASE_URL}/auth/login`,
    JSON.stringify({
      email: creds.email,
      password: creds.password
    }),
    json
  );

  console.log(
    `Login ${creds.email}: status=${res.status}, body=${res.body}`
  );

  if (res.status !== 200) {
    throw new Error(`Login failed for ${creds.email}: HTTP ${res.status}`);
  }

  const body = res.json();
  const token = body.token;

  if (!token) {
    throw new Error('Login response does not contain a token field');
  }

  return token;
}


function auth(token) {
  return { headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${token}` } };
}


export function setup() {
  // Register user; login will work if the account already exists
  http.post(`${BASE_URL}/auth/register`, JSON.stringify(USER), json);

  const userToken = login(USER);
  const adminToken = login(ADMIN);

  const eventRes = http.post(
    `${BASE_URL}/api/admin/events`,
    JSON.stringify({
      title: 'Load Test Event',
      location: 'Test Arena',
      eventDateTime: '2027-01-01T18:00:00',
      totalSeats: TOTAL_SEATS,
      ticketPrice: 500,
    }),
    auth(adminToken)
  );

  console.log(`Event response: status=${eventRes.status}, body=${eventRes.body}`);

  if (eventRes.status !== 200 && eventRes.status !== 201) {
    throw new Error(`Event creation failed: HTTP ${eventRes.status}`);
  }

  const eventBody = eventRes.json();
  const eventId = eventBody.data.id;
  const seatIds = eventBody.data.seats.map((seat) => seat.id);

  console.log(`Event ${eventId} created with ${seatIds.length} seats`);

  return { userToken, eventId, seatIds };
}



export function browse() {
  const res = http.get(`${BASE_URL}/api/events`);

  check(res, {
    'events 200': (r) => r.status === 200,
  });

  if (res.status !== 200) {
    console.error(
      `GET /api/events failed: status=${res.status}, body=${res.body}`
    );
  }

  sleep(1);
}


export function book(data) {
  // each iteration in the whole test takes its own seat, so seats never clash
  const i = exec.scenario.iterationInTest;
  const res = http.post(`${BASE_URL}/api/bookings`,
    JSON.stringify({ eventId: data.eventId, seatIds: [data.seatIds[i]] }),
    auth(data.userToken));
  check(res, { 'booking 200/201': (r) => r.status === 200 || r.status === 201 });
  sleep(1);
}