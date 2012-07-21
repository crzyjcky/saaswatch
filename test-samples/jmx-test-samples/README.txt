start java -D -Dcom.sun.management.jmxremote.authenticate=false  -Dcom.sun.management.jmxremote.port=9999  -Dcom.sun.management.jmxremote.ss
l=false DummyApp


curl http://localhost:9091/memoryResource/v0/nonHeapMemory
curl http://localhost:9091/memoryResource/v0/heapMemory