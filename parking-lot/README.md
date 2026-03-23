
## Design Approach

The system is split into small, focused classes — each one does exactly one thing.

**Vehicle & Enums** — A `Vehicle` holds a license plate and type. `VehicleType` and `SlotType` are enums so we don't pass around raw strings.

**ParkingSlot** — Knows its number, type (SMALL/MEDIUM/LARGE), and whether it's occupied. The slot itself handles occupy/free.

**EntryGate** — Each gate stores a distance map to every slot. This is how we figure out which slot is "nearest" from a given entrance.

**ParkingTicket** — A snapshot of what happened when the vehicle entered: which vehicle, which slot, what time. This is the receipt the driver holds.

**SlotAssignmentStrategy (interface)** — Decouples the "how do we pick a slot" logic from the parking lot itself. Right now we have `NearestSlotStrategy`, but you could swap in a different one (e.g., random, load-balanced) without touching `ParkingLot`.

**NearestSlotStrategy** — Loops through all free slots, filters by compatibility (bikes can go in small/medium/large, cars in medium/large, buses only large), and picks the one closest to the entry gate.

**BillingService** — Stores hourly rates per slot type. Billing is based on the slot type the vehicle was parked in, not the vehicle type. So a bike in a medium slot pays the medium rate.

**ParkingLot** — The main orchestrator. It holds the slots, gates, and active tickets. The three APIs:
- `park()` — finds a slot via the strategy, marks it occupied, creates a ticket
- `status()` — counts total vs available slots per type
- `exit()` — calculates the bill, frees the slot, removes the ticket

## How to Run

```bash
cd parking-lot
javac -d out src/com/example/parking/*.java
java -cp out com.example.parking.ParkingLotTest
```
