var webpack = require('webpack');

module.exports = require('./scalajs.webpack.config');

const Path = require('path');
const rootDir = Path.resolve(__dirname, '../../../..');
module.exports.devServer = {
    contentBase: [
           Path.resolve(module.exports.output.path, 'dev'), // fastOptJS output
           Path.resolve(rootDir, 'assets') // project root containing index.html
    ],
    watchContentBase: true,
    hot: true,
    hotOnly: false, // only reload when build is successful
    inline: true // live reloading
};

module.exports.plugins = [
    new webpack.HotModuleReplacementPlugin()
];
