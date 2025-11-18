import { createFileRoute } from '@tanstack/react-router';

export const Route = createFileRoute('/offers/$offerId/')({
  component: OfferItemDetails,
});

function OfferItemDetails() {
  return (
    <main className="py-5 px-2">
      <div className="max-w-[1200px] mx-auto"></div>
    </main>
  );
}
