# Simulation Mode Issue

Change `src/main/java/org/group_mmm/SimulinkSUL.java`

```java
/// Configuration on the accelerator
// Use normal mode
// builder.append("set_param(mdl,'SimulationMode','normal');");
// Enable accelerator mode
builder.append("set_param(mdl,'SimulationMode','accelerator');");
// Enable classic accelerator mode
builder.append("set_param(0, 'GlobalUseClassicAccelMode', 'on');");
```

to

```java
/// Configuration on the accelerator
// Use normal mode
builder.append("set_param(mdl,'SimulationMode','normal');");
// Enable accelerator mode
// builder.append("set_param(mdl,'SimulationMode','accelerator');");
// Enable classic accelerator mode
builder.append("set_param(0, 'GlobalUseClassicAccelMode', 'on');");
```

then re-install FalCAuN

```bash
mvn clean
mvn install
```
