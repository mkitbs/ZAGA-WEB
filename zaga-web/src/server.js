const express = require('express');

const app = express();

app.use(express.static('./dist/zaga-veb'));

app.get('/*', (req, res) =>
    res.sendFile('index.html', {root: 'dist/zaga-veb/'}),
);

app.listen(process.env.PORT || 8080);