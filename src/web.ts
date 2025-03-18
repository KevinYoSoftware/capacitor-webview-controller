import { WebPlugin } from '@capacitor/core';

import type { WebViewControllerPlugin } from './definitions';

export class WebViewControllerWeb extends WebPlugin implements WebViewControllerPlugin {
  async setKeepScreenAwake(_options: { enable: boolean }): Promise<void> {
    console.warn('WebViewController.setKeepScreenAwake: This method is not available on web');
    return;
  }

  async getSettings(): Promise<{ keepScreenOn: boolean }> {
    console.warn('WebViewController.getSettings: This method is not available on web');
    return { keepScreenOn: false };
  }
  
}