import { WebPlugin } from '@capacitor/core';

import type { WebViewControllerPlugin } from './definitions';

export class WebViewControllerWeb extends WebPlugin implements WebViewControllerPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
