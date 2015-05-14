namespace java am.ik.categolj2.thrift


struct TEntry {
  1: i32 entryId,
  2: string title,
  3: string contents
}

service TCategolj2 {
  TEntry findOne(1: i32 entryId)
}