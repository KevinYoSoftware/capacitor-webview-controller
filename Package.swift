// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorWebviewController",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorWebviewController",
            targets: ["WebViewControllerPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "WebViewControllerPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/WebViewControllerPlugin"),
        .testTarget(
            name: "WebViewControllerPluginTests",
            dependencies: ["WebViewControllerPlugin"],
            path: "ios/Tests/WebViewControllerPluginTests")
    ]
)