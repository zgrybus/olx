export const getMockOffersList = ({ count }: { count: number }) =>
  Array.from({ length: count }, (_, i) => ({
    id: i,
    title: `title_${i}`,
    description: `id_${i}`,
    createdAt: new Date(2024 - i, 5, 5).toDateString(),
    updatedAt: new Date(2025 - i, 5, 5).toDateString(),
    price: 1000 - i,
  }));
