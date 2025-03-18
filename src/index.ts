import { registerPlugin } from '@capacitor/core';

import type { WebViewControllerPlugin } from './definitions';

const WebViewController = registerPlugin<WebViewControllerPlugin>('WebViewController', {
  web: () => import('./web').then((m) => new m.WebViewControllerWeb()),
});

export * from './definitions';
export { WebViewController };
