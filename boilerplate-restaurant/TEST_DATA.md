# Test Data

This document describes a sample dataset you can seed into the database to
exercise the Restaurant Table Reservation System API.

> **Note:** This is just an **example** dataset that covers the main scenarios
> the API must handle. You are free to extend it, replace it, or add additional
> data that helps you build and verify your implementation.

## Assumptions

The problem statement leaves several details open. The dataset below assumes:

- Service runs from **18:00 to 22:00** in four one-hour slots: `18:00`, `19:00`,
  `20:00`, `21:00`.
- Each reservation occupies exactly one slot.
- A reservation in `CANCELLED` status does not block the table.

Feel free to change these assumptions — document your choices in the README.

## Restaurant Tables

| Name | Capacity |
|------|----------|
| T1   | 2        |
| T2   | 4        |
| T3   | 4        |
| T4   | 6        |
| T5   | 8        |

## Existing Reservations

| Table | Date       | Slot  | Guests | Customer       | Contact             | Notes          | Status    |
|-------|------------|-------|--------|----------------|---------------------|----------------|-----------|
| T2    | 2026-06-15 | 19:00 | 2      | Alice Sharma   | alice@example.com   | window seat    | CONFIRMED |
| T3    | 2026-06-15 | 19:00 | 4      | Bob Patel      | +91-9876543210      | birthday cake  | CONFIRMED |
| T4    | 2026-06-15 | 20:00 | 4      | Carol Mehta    | carol@example.com   |                | CONFIRMED |
| T5    | 2026-06-15 | 18:00 | 3      | Dev Iyer       | +91-9123456780      |                | CONFIRMED |
| T5    | 2026-06-15 | 19:00 | 3      | Esha Reddy     | esha@example.com    | quiet corner   | CONFIRMED |
| T5    | 2026-06-15 | 19:00 | 2      | Gita Nair      | gita@example.com    |                | CONFIRMED |
| T5    | 2026-06-15 | 21:00 | 2      | Harish Joshi   | harish@example.com  |                | CONFIRMED |
| T1    | 2026-06-15 | 18:00 | 2      | Farid Khan     | farid@example.com   |                | CANCELLED |
| T3    | 2026-06-16 | 19:00 | 4      | Ira Singh      | ira@example.com     |                | CONFIRMED |

## Scenarios this data covers

| Scenario                                                              | What to look at                                                  |
|-----------------------------------------------------------------------|------------------------------------------------------------------|
| A fully available table at a slot                                     | T1 on `2026-06-15` (no confirmed bookings)                       |
| A fully booked table → must NOT appear in availability                | T3 @ 19:00 on `2026-06-15` (4/4 booked)                          |
| A partially filled table where the party fits (Advanced)              | T2 @ 19:00 on `2026-06-15` → 2 remaining seats                   |
| A partially filled table where the party does NOT fit                 | T2 @ 19:00 on `2026-06-15` with `partySize=3` → must be excluded |
| Aggregation across multiple bookings on the same (table, slot)        | T5 @ 19:00 on `2026-06-15` (3 + 2 = 5 booked → 3 remaining)       |
| Cancelled reservations do not block the table                         | T1 @ 18:00 on `2026-06-15`                                       |
| Date filtering — a different date's booking must not leak in          | T3 @ 19:00 on `2026-06-16` should not affect `2026-06-15` views  |

Feel free to add more rows to cover any additional cases you want to verify
(e.g. cancellation policy, race-condition tests in the Book Table API, etc.).
