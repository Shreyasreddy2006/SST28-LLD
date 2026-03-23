
## Design Approach

The whole idea is that a pen has three independent behaviors — how it writes, how it refills, and how it opens/closes. Each of these can vary independently, so they're each behind their own interface.

**WriteBehavior** — How the pen puts ink on paper. Ballpoint rolls a ball, gel pushes thick ink, fountain flows through a nib. Adding a new write style (say, marker) means writing one new class. Nothing else changes.

**RefillBehavior** — How you reload ink. Ballpoint and gel pens swap a cartridge. Fountain pens draw from a bottle. Again, adding a new refill method is just one class.

**OpenCloseBehavior** — How you expose the tip. Cap pens pull a cap off. Click pens push a button. The pen itself doesn't care which — it just delegates.

**Pen** — Holds a color, a type, and three behaviors. It tracks whether it's opened or closed. If you try to write with a closed pen, it throws an error. Refilling swaps the ink color.

**PenFactory** — Wires everything together. You say "give me a gel pen, blue, with click mechanism" and it picks the right WriteBehavior, RefillBehavior, and OpenCloseBehavior. The Pen class never has to know about specific implementations.

**Why this works well** — Want to add a marker pen? Write one `MarkerWrite` class and add a case in the factory. Want to add a twist mechanism? Write one `TwistMechanism` class and add a case. The `Pen` class stays untouched. That's the Open/Closed Principle in action.

## How to Run

```bash
cd pen
javac -d out src/com/example/pen/*.java
java -cp out com.example.pen.PenTest
```
