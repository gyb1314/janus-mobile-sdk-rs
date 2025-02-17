// swift-tools-version: 6.0
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

// Never push to remote with this flag set to true
let useLocalFramework = false
let releaseTag = "0.7.1"
let releaseChecksum = "4c17b0589839416fa289e8e1b8d1edef52785ed0f79f934613951164d55af070"

let binaryTarget: Target = if useLocalFramework {
    .binaryTarget(
        name: "JanusGatewayFFI",
        path: "./target/ios/libjanus_gateway-rs.xcframework"
    )
} else {
    .binaryTarget(
        name: "JanusGatewayFFI",
        url: "https://github.com/Ghamza-Jd/janus-mobile-sdk/releases/download/\(releaseTag)/libjanus_gateway-rs.xcframework.zip",
        checksum: releaseChecksum
    )
}

let package = Package(
    name: "JanusGateway",
    platforms: [.iOS(.v13)],
    products: [
        .library(name: "JanusGateway", targets: ["JanusGateway"]),
        .library(name: "JanusGatewayPlugins", targets: ["JanusGatewayPlugins"]),
    ],
    targets: [
        binaryTarget,
        .target(
            name: "JanusGateway",
            dependencies: [.target(name: "JanusGatewayBindings")],
            path: "apple/Sources/JanusGateway",
            resources: [
                .process("Resources/PrivacyInfo.xcprivacy")
            ]
        ),
        .target(
            name: "JanusGatewayPlugins",
            dependencies: [
                .target(name: "JanusGatewayBindings"),
                .target(name: "JanusGateway")
            ],
            path: "apple/Sources/Plugins",
            resources: [
                .process("Resources/PrivacyInfo.xcprivacy")
            ]
        ),
        .target(
            name: "JanusGatewayBindings",
            dependencies: [.target(name: "JanusGatewayFFI")],
            path: "apple/Sources/Bindings"
        ),
        .testTarget(
            name: "JanusGatewayTests",
            dependencies: ["JanusGateway"],
            path: "apple/Tests/JanusGatewayTests"
        ),
    ]
)
