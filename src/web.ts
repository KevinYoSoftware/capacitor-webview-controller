import { WebPlugin } from '@capacitor/core';

import type { WebViewControllerPlugin } from './definitions';

export class WebViewControllerWeb extends WebPlugin implements WebViewControllerPlugin {
  async setKeepScreenAwake(_options: { enable: boolean }): Promise<void> {
    console.warn('WebViewController.setKeepScreenAwake: This method is not available on web');
    return;
  }

  async getSettings(): Promise<{ keepScreenOn: boolean; isDeviceOwner: boolean; lockTaskActive: boolean }> {
    console.warn('WebViewController.getSettings: This method is not available on web');
    return { keepScreenOn: false, isDeviceOwner: false, lockTaskActive: false };
  }

  async isDeviceOwner(): Promise<{ value: boolean }> {
    console.warn('WebViewController.isDeviceOwner is not implemented on web');
    return { value: false };
  }

  async setLockTaskPackages(): Promise<{ value: boolean }> {
    console.warn('WebViewController.setLockTaskPackages is not implemented on web');
    return { value: false };
  }
  async startLockTask(): Promise<{ value: boolean }> {
    console.warn('WebViewController.startLockTask is not implemented on web');
    return { value: false };
  }
  async stopLockTask(): Promise<{ value: boolean }> {
    console.warn('WebViewController.stopLockTask is not implemented on web');
    return { value: false };
  }
  async isLockTaskActive(): Promise<{ value: boolean }> {
    console.warn('WebViewController.isLockTaskActive is not implemented on web');
    return { value: false };
  }
}
