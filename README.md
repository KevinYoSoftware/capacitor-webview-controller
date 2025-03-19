# capacitor-webview-controller

Capacitor plugin to control WebView settings across contexts

## Install

```bash
npm install capacitor-webview-controller
npx cap sync
```

## API

<docgen-index>

* [`setKeepScreenAwake(...)`](#setkeepscreenawake)
* [`getSettings()`](#getsettings)
* [`isDeviceOwner()`](#isdeviceowner)
* [`setLockTaskPackages()`](#setlocktaskpackages)
* [`startLockTask()`](#startlocktask)
* [`stopLockTask()`](#stoplocktask)
* [`isLockTaskActive()`](#islocktaskactive)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### setKeepScreenAwake(...)

```typescript
setKeepScreenAwake(options: { enable: boolean; }) => Promise<void>
```

Set whether to keep the screen awake while the application is active.
This prevents the device from automatically dimming and locking the screen.

| Param         | Type                              | Description                                                   |
| ------------- | --------------------------------- | ------------------------------------------------------------- |
| **`options`** | <code>{ enable: boolean; }</code> | Object containing enable flag to control screen wake behavior |

--------------------


### getSettings()

```typescript
getSettings() => Promise<{ keepScreenOn: boolean; isDeviceOwner: boolean; lockTaskActive: boolean; }>
```

Get comprehensive settings and state information about the WebView controller.
This includes screen wake settings and kiosk mode (Lock Task) state information.

**Returns:** <code>Promise&lt;{ keepScreenOn: boolean; isDeviceOwner: boolean; lockTaskActive: boolean; }&gt;</code>

--------------------


### isDeviceOwner()

```typescript
isDeviceOwner() => Promise<{ value: boolean; }>
```

Check if the application is running as a Device Owner.
Device Owner status enables enhanced kiosk functionality without popups.

**Returns:** <code>Promise&lt;{ value: boolean; }&gt;</code>

--------------------


### setLockTaskPackages()

```typescript
setLockTaskPackages() => Promise<{ value: boolean; }>
```

Set this app as the only one allowed in Lock Task Mode.
Only works if the app is a Device Owner. This must be called before
startLockTask() to enable enhanced kiosk mode with no exit popups.

**Returns:** <code>Promise&lt;{ value: boolean; }&gt;</code>

--------------------


### startLockTask()

```typescript
startLockTask() => Promise<{ value: boolean; }>
```

Start Lock Task Mode (kiosk mode).
If the app is a Device Owner and setLockTaskPackages() was called, 
this will enable enhanced kiosk mode without exit popups.
Otherwise, it enables basic kiosk mode with exit instructions.

**Returns:** <code>Promise&lt;{ value: boolean; }&gt;</code>

--------------------


### stopLockTask()

```typescript
stopLockTask() => Promise<{ value: boolean; }>
```

Stop Lock Task Mode (exit kiosk mode).
This should be called when exiting kiosk mode, typically after
successful authentication in restricted areas of the app.

**Returns:** <code>Promise&lt;{ value: boolean; }&gt;</code>

--------------------


### isLockTaskActive()

```typescript
isLockTaskActive() => Promise<{ value: boolean; }>
```

Check if Lock Task Mode is currently active.
This can be used to determine the current kiosk state of the application.

**Returns:** <code>Promise&lt;{ value: boolean; }&gt;</code>

--------------------

</docgen-api>
