export interface WebViewControllerPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
