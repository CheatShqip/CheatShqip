var body = JSON.stringify({
    priority: 0,
    request: {
        method: 'GET',
        urlPathPattern: '/define/karte'
    },
    response: {
        status: 500
    }
});
http.post('http://localhost:9090/__admin/mappings', { body: body, headers: { 'Content-Type': 'application/json' } });
