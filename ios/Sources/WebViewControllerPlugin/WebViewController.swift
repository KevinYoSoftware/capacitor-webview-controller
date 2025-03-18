import Foundation

@objc public class WebViewController: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
