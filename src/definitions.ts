export interface WebViewControllerPlugin {
  /**
   * Set whether to keep the screen awake while the application is active.
   * This prevents the device from automatically dimming and locking the screen.
   * 
   * @param options Object containing enable flag to control screen wake behavior
   * @returns Promise that resolves when the setting has been applied
   */
  setKeepScreenAwake(options: { enable: boolean }): Promise<void>;

  /**
   * Get comprehensive settings and state information about the WebView controller.
   * This includes screen wake settings and kiosk mode (Lock Task) state information.
   * 
   * @returns Promise that resolves with an object containing:
   *   - keepScreenOn: Whether the screen is prevented from sleeping
   *   - isDeviceOwner: Whether the app is running with Device Owner privileges
   *   - lockTaskActive: Whether Lock Task Mode (kiosk mode) is currently active
   */
  getSettings(): Promise<{ 
    keepScreenOn: boolean;
    isDeviceOwner: boolean;
    lockTaskActive: boolean;
  }>;

  /**
   * Check if the application is running as a Device Owner.
   * Device Owner status enables enhanced kiosk functionality without popups.
   * 
   * @returns Promise that resolves with an object containing the device owner status
   */
  isDeviceOwner(): Promise<{ value: boolean }>;

  /**
   * Set this app as the only one allowed in Lock Task Mode.
   * Only works if the app is a Device Owner. This must be called before
   * startLockTask() to enable enhanced kiosk mode with no exit popups.
   * 
   * @returns Promise that resolves with success status of the operation
   */
  setLockTaskPackages(): Promise<{ value: boolean }>;

  /**
   * Start Lock Task Mode (kiosk mode).
   * If the app is a Device Owner and setLockTaskPackages() was called, 
   * this will enable enhanced kiosk mode without exit popups.
   * Otherwise, it enables basic kiosk mode with exit instructions.
   * 
   * @returns Promise that resolves with success status of the operation
   */
  startLockTask(): Promise<{ value: boolean }>;

  /**
   * Stop Lock Task Mode (exit kiosk mode).
   * This should be called when exiting kiosk mode, typically after
   * successful authentication in restricted areas of the app.
   * 
   * @returns Promise that resolves with success status of the operation
   */
  stopLockTask(): Promise<{ value: boolean }>;

  /**
   * Check if Lock Task Mode is currently active.
   * This can be used to determine the current kiosk state of the application.
   * 
   * @returns Promise that resolves with the current Lock Task Mode status
   */
  isLockTaskActive(): Promise<{ value: boolean }>;

  clearDeviceOwner(options: { confirm: boolean }): Promise<{ value: boolean, message: string }>;


}