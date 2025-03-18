export interface WebViewControllerPlugin {
  
  /**
   * Set whether to keep the screen awake while InAppBrowser is active
   * @param options Options with enable property
   */
  setKeepScreenAwake(options: { enable: boolean }): Promise<void>;
  
  /**
   * Get current settings of the WebView controller
   * @returns Current settings
   */
  getSettings(): Promise<{ keepScreenOn: boolean }>;
  
}