declare let process: any;
console.log(process.env.NODE_ENV)
export const environment = {
  production: process.env.NODE_ENV == 'production',
  apiUrl: process.env.NODE_ENV == 'production' ? 'https://dogosydungbe.azurewebsites.net/' : 'http://localhost:8080/'
};