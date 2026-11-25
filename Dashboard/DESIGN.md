---
name: Atmospheric Telemetry Console
colors:
  surface: '#121318'
  surface-dim: '#121318'
  surface-bright: '#38393f'
  surface-container-lowest: '#0d0e13'
  surface-container-low: '#1a1b21'
  surface-container: '#1e1f25'
  surface-container-high: '#292a2f'
  surface-container-highest: '#34343a'
  on-surface: '#e3e1e9'
  on-surface-variant: '#d8c3ad'
  inverse-surface: '#e3e1e9'
  inverse-on-surface: '#2f3036'
  outline: '#a08e7a'
  outline-variant: '#534434'
  surface-tint: '#ffb95f'
  primary: '#ffc174'
  on-primary: '#472a00'
  primary-container: '#f59e0b'
  on-primary-container: '#613b00'
  inverse-primary: '#855300'
  secondary: '#c0c7d3'
  on-secondary: '#2a313b'
  secondary-container: '#404752'
  on-secondary-container: '#afb5c2'
  tertiary: '#8fd5ff'
  on-tertiary: '#00344a'
  tertiary-container: '#1abdff'
  on-tertiary-container: '#004966'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#ffddb8'
  primary-fixed-dim: '#ffb95f'
  on-primary-fixed: '#2a1700'
  on-primary-fixed-variant: '#653e00'
  secondary-fixed: '#dce3f0'
  secondary-fixed-dim: '#c0c7d3'
  on-secondary-fixed: '#151c25'
  on-secondary-fixed-variant: '#404752'
  tertiary-fixed: '#c5e7ff'
  tertiary-fixed-dim: '#7fd0ff'
  on-tertiary-fixed: '#001e2d'
  on-tertiary-fixed-variant: '#004c6a'
  background: '#121318'
  on-background: '#e3e1e9'
  surface-variant: '#34343a'
typography:
  headline-xl:
    fontFamily: Space Grotesk
    fontSize: 3rem
    fontWeight: '600'
    lineHeight: 3.25rem
    letterSpacing: -0.04em
  headline-xl-mobile:
    fontFamily: Space Grotesk
    fontSize: 2rem
    fontWeight: '600'
    lineHeight: 2.25rem
    letterSpacing: -0.03em
  headline-lg:
    fontFamily: Space Grotesk
    fontSize: 2rem
    fontWeight: '500'
    lineHeight: 2.25rem
    letterSpacing: -0.02em
  headline-md:
    fontFamily: Space Grotesk
    fontSize: 1.5rem
    fontWeight: '500'
    lineHeight: 1.75rem
    letterSpacing: -0.02em
  headline-sm:
    fontFamily: Space Grotesk
    fontSize: 1.125rem
    fontWeight: '500'
    lineHeight: 1.5rem
    letterSpacing: -0.01em
  body-lg:
    fontFamily: Geist
    fontSize: 1rem
    fontWeight: '400'
    lineHeight: 1.5rem
    letterSpacing: 0em
  body-md:
    fontFamily: Geist
    fontSize: 0.875rem
    fontWeight: '400'
    lineHeight: 1.25rem
    letterSpacing: 0.005em
  body-sm:
    fontFamily: Geist
    fontSize: 0.75rem
    fontWeight: '400'
    lineHeight: 1rem
    letterSpacing: 0.01em
  label-lg:
    fontFamily: Space Grotesk
    fontSize: 0.875rem
    fontWeight: '600'
    lineHeight: 1rem
    letterSpacing: 0.08em
  label-md:
    fontFamily: Space Grotesk
    fontSize: 0.75rem
    fontWeight: '600'
    lineHeight: 0.875rem
    letterSpacing: 0.1em
  label-sm:
    fontFamily: Space Grotesk
    fontSize: 0.625rem
    fontWeight: '700'
    lineHeight: 0.75rem
    letterSpacing: 0.12em
spacing:
  gutter: 1px
  gutter-tablet: 1px
  gutter-mobile: 1px
  margin: 1.5rem
  margin-mobile: 0.75rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2.5rem
---

## Brand & Style

The design system embodies an uncompromising, high-precision meteorological console. It serves researchers, aviators, and hyper-local atmospheric observers who require instant, zero-latency situational awareness without superficial ornamentation.

The visual direction is rooted in technical brutalism fused with refined aerospace ergonomics. It eliminates decorative soft radii and superfluous drop shadows, prioritizing razor-sharp structural integrity, monoline instrumentation, and high-density information architecture. Every interface boundary behaves as a functional bulkhead; every numerical datum carries the gravitas of calibrated physical measurement. The aesthetic evokes a quiet, nighttime control station—restrained, dark, luminous only where atmospheric alerts demand human intervention.

## Colors

The palette simulates high-contrast cockpit displays calibrated for low-light observation. The ground is formed by `#090A0F`, an obsidian void that eliminates display glare. Layered architectural tiers utilize slightly elevated slate tones (`#0E1117`, `#151922`) to demarcate functional panels without relying on visual noise.

Primary accents leverage an intense atmospheric amber spectrum (`#F59E0B`, `#FBBF24`, `#D97706`). Amber is reserved strictly for real-time telemetry focus, solar/thermal indicators, dynamic barometric thresholds, and critical operational triggers. Off-white typographic tokens (`#F3F4F6` and secondary `#9CA3AF`) provide crystal clarity without eye strain. Structural separation is maintained through micro-borders tinted to `rgba(255, 255, 255, 0.07)` or solid `#1F2937`.

## Typography

The typographic hierarchy pairs two technical typefaces with distinct functions:

- **Space Grotesk** commands headlines, index labels, coordinate metrics, and telemetry readings. Its geometric rigor and subtle engineered anomalies evoke mechanical avionics. All technical labels must be typeset in uppercase with generous tracking (`0.08em` to `0.12em`) to replicate instrument dial engravings.
- **Geist** manages continuous analytical readouts, status feeds, and secondary descriptions. Its neutral, high-legibility construction prevents visual fatigue during dense tabular inspection.

Numerical values inside gauges, graphs, and live monitors must enforce tabular figures (`tnum`) to eliminate layout shift during rapid meteorological data recalculations.

## Layout & Spacing

The interface employs a continuous interlocking matrix rather than floating, spaced-out elements. Surfaces touch edge-to-edge; layout separations are enforced via hairline `1px` structural gutters that expose the substrate border `#1F2937` or `rgba(255, 255, 255, 0.07)`.

- **Desktop (1440px+)**: A strict 12-column modular cockpit grid. Data telemetry panels occupy 3, 6, or 12 column modules, snapping edge-to-edge. Canvas margins are fixed at `1.5rem`.
- **Tablet (768px – 1439px)**: Collapses into an 8-column structural grid; primary atmospheric charts lock to full-width or half-width splits.
- **Mobile (< 768px)**: A single-column vertical telemetry stack. Canvas margin reduces to `0.75rem`. Dense modules compress internal padding (`space-xs`, `space-sm`) while preserving line readability.

## Elevation & Depth

This design system rejects conventional dropped shadows and floating blurred cards. In an aerospace console, depth is expressed entirely through tonal bulkhead tiers and low-contrast perimeter micro-lines.

- **Baseplate (Level 0)**: Background `#090A0F`. The foundational canvas holding the framing matrix.
- **Instrument Surface (Level 1)**: Fill `#0E1117` encapsulated by a hairline `1px` border (`rgba(255, 255, 255, 0.07)`). Used for modular telemetry monitors, parameter lists, and secondary readings.
- **Active Focus Surface (Level 2)**: Fill `#151922` bounded by an amber-accented border (`#F59E0B` at `30%` opacity). Used for primary dynamic charts, active Doppler radar overlays, or interactive input areas.
- **Overlay & HUD Modals (Level 3)**: Fill `#0E1117` with absolute zero blur, bounded by a continuous `1px` border in `#F59E0B`. High-priority alert modules receive an interior hairline inset (`inset 0 0 0 1px rgba(245, 158, 11, 0.2)`).

## Shapes

The shape system strictly uses 0px roundedness across the entire component library. Curves are prohibited in structural boundaries, inputs, badges, and viewports.

Every container, button, tooltip, alert tag, and modal exists as a pure rectangle. Visual rhythm is introduced through precision angular cuts:
- Interactive panels may feature a `4px` 45-degree chamfered corner at the top-right or bottom-left corner (`clip-path: polygon(...)`).
- Technical brackets (`[` and `]`) or corner hairpins (`crosshairs`) may frame key metric displays, emphasizing laboratory-grade hardware construction.

## Components

### Buttons & Trigger Controls
- **Primary Action**: Sharp rectangular container, background `#F59E0B`, text `#090A0F` in uppercase `Space Grotesk` (font weight 600, letter spacing `0.1em`). Hover state transitions background to `#FBBF24`. Active state steps down to `#D97706`.
- **Secondary / Telemetry Trigger**: Background `#0E1117`, border `1px solid rgba(255, 255, 255, 0.12)`, text `#F3F4F6`. Hover introduces an amber border edge (`#F59E0B`) and amber typography.
- **Destructive Action**: Background transparent, border `1px solid #EF4444`, text `#EF4444`.

### Metrics & Data Cards
- Pure zero-radius rectangular containers backed by `#0E1117`.
- Top edge features an uppercase micro-label (`label-sm`, `#9CA3AF`) accompanied by a terminal index (e.g., `// BARO_01`).
- Numerical values scale into `Space Grotesk` with fixed tab-digits (`font-variant-numeric: tabular-nums`).
- Trend indicators employ clean 1px monoline vector ticks or minimal glyphs without soft pill enclosures.

### Telemetry Gauges & Charts
- Line charts operate with razor-thin `1.5px` monoline paths in `#F59E0B` across dark `#0E1117` canvas plates.
- Horizontal and vertical grid lines are rendered in dashed `rgba(255, 255, 255, 0.05)`.
- Radial gauges discard thick rounded tracks in favor of segmented mechanical ticks or stepped arc arrays.

### Chips & Status Tags
- Monospaced rectangular badges with zero corner radius.
- Padding: `0.125rem 0.5rem`.
- Status `NORMAL`: Border `1px solid rgba(255, 255, 255, 0.1)`, text `#9CA3AF`, background `transparent`.
- Status `WARNING / ACTIVE`: Border `1px solid #F59E0B`, text `#F59E0B`, background `rgba(245, 158, 11, 0.08)`.

### Form Fields & Inputs
- Enclosed rectangular inputs with solid background `#090A0F` and `1px` border `rgba(255, 255, 255, 0.12)`.
- Focus shifts the perimeter outline to `#F59E0B` without outer fuzzy glow.
- Cursor is styled as an amber vertical bar.

### Checkboxes & Toggle Switches
- Checkboxes: 14x14px square boxes, zero border radius, border `1px solid rgba(255, 255, 255, 0.2)`. Checked state displays a solid `#F59E0B` fill with an obsidian geometric tick.
- Toggles: Segmented rectangular two-state rockers instead of rounded pills. The active state snaps to a solid `#F59E0B` fill with `#090A0F` typography.

### Lists & Readout Feeds
- Telemetry log entries are separated by `1px` hairline rules in `rgba(255, 255, 255, 0.05)`.
- Timestamp column in `Space Grotesk` (`#9CA3AF`), followed by technical parameter descriptor and live reading.
- Alternating striping is prohibited; state variation is communicated via left-aligned `2px` amber marker rules.