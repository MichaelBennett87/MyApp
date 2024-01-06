const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require('copy-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true,
  },
  entry: {
    examplePage: path.resolve(__dirname, 'src', 'pages', 'examplePage.js'),
    userPage: path.resolve(__dirname, 'src', 'pages', 'userPage.js'),
    petPage: path.resolve(__dirname, 'src', 'pages', 'petPage.js'),
    petDashboardPage: path.resolve(__dirname, 'src', 'pages', 'petDashboardPage.js'),
    displayPetPage: path.resolve(__dirname, 'src', 'pages', 'displayPetPage.js'),
    chat: path.resolve(__dirname, 'src', 'pages', 'chat.js'),
    review: path.resolve(__dirname, 'src', 'pages', 'review.js'),
    customizePetPage: path.resolve(__dirname, 'src', 'pages', 'customizePetPage.js'),
  },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    static: {
      directory: path.resolve(__dirname, 'dist'), // Specify the directory where your static files are located
    },
    https: false,
    port: 8080,
    open: true,
    proxy: [
      {
        context: ['/example', '/user', '/pet', '/displayPet', '/customizePet'],
        target: 'http://localhost:5001',
      },
    ],
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader',
          options: {
            presets: ['@babel/preset-env', '@babel/preset-react'],
          },
        },
      },
    ],
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      inject: false,
    }),
    new HtmlWebpackPlugin({
      template: './src/register.html',
      filename: 'register.html',
      inject: false,
    }),
    new HtmlWebpackPlugin({
      template: './src/login.html',
      filename: 'login.html',
      inject: false,
    }),
    new HtmlWebpackPlugin({
      template: './src/adoption.html',
      filename: 'adoption.html',
      inject: false,
    }),
      new HtmlWebpackPlugin({
      template: './src/petDashboard.html',
      filename: 'petDashboard.html',
      inject: false,
    }),
    new HtmlWebpackPlugin({
      template: './src/customizePet.html',
      filename: 'customizePet.html',
      inject: false,
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve('dist/css'),
        },
      ],
    }),
    new CleanWebpackPlugin(),
  ],
};
