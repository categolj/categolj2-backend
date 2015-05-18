namespace java am.ik.categolj2.thrift


struct TEntry {
  1: i32 entryId,
  2: string title,
  3: string contents
}

struct TLink {
  1: string url,
  2: string linkName
}

exception TCategolj2ClientException {
  1: required string errorCode;
  2: required string errorMessage;
}

exception TCategolj2ServerException {
  1: required string errorCode;
  2: required string errorMessage;
}

service TCategolj2 {
  TEntry findOnePublishedEntry(1: i32 entryId) throws (1: TCategolj2ClientException clientException, 2: TCategolj2ServerException serverException),
  list<TLink> findAllLinks()
}
