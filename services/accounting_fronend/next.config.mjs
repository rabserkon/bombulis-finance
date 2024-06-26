/** @type {import('next').NextConfig} */
const nextConfig = {
    async rewrites() {
        return [
            {
                source: '/api/user/v1/:path*',
                destination: 'http://localhost:8087/:path*',
            },
            {
                source: '/api/finance/v1/:path*',
                destination: 'http://localhost:8087/:path*',
            },
            {
                source: '/api/auth/v1/:path*',
                destination: 'http://localhost:8087/:path*',
            },
        ];
    },
    env: {
        auth_url_module: 'http://localhost:8087',
        finance_url_module: 'http://localhost:8087',
    },
    transpilePackages: [
        "@ant-design",
        "@rc-component",
        "antd",
        "rc-cascader",
        "rc-checkbox",
        "rc-collapse",
        "rc-dialog",
        "rc-drawer",
        "rc-dropdown",
        "rc-field-form",
        "rc-image",
        "rc-input",
        "rc-input-number",
        "rc-mentions",
        "rc-menu",
        "rc-motion",
        "rc-notification",
        "rc-pagination",
        "rc-picker",
        "rc-progress",
        "rc-rate",
        "rc-resize-observer",
        "rc-segmented",
        "rc-select",
        "rc-slider",
        "rc-steps",
        "rc-switch",
        "rc-table",
        "rc-tabs",
        "rc-textarea",
        "rc-tooltip",
        "rc-tree",
        "rc-tree-select",
        "rc-upload",
        "rc-util",
    ],
};

export default nextConfig;