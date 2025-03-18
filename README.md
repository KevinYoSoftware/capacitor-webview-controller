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

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### setKeepScreenAwake(...)

```typescript
setKeepScreenAwake(options: { enable: boolean; }) => Promise<void>
```

Set whether to keep the screen awake while InAppBrowser is active

| Param         | Type                              | Description                  |
| ------------- | --------------------------------- | ---------------------------- |
| **`options`** | <code>{ enable: boolean; }</code> | Options with enable property |

--------------------


### getSettings()

```typescript
getSettings() => Promise<{ keepScreenOn: boolean; }>
```

Get current settings of the WebView controller

**Returns:** <code>Promise&lt;{ keepScreenOn: boolean; }&gt;</code>

--------------------

</docgen-api>
