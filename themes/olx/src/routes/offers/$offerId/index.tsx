import { Link, createFileRoute } from '@tanstack/react-router';
import { ChevronLeft } from 'lucide-react';
import { $olxApi } from '@/apis/olx';
import { dateTimeFormatUtil } from '@/features/date-utils/date-time-format-util';
import { priceFormatUtil } from '@/features/number-utils/number-format-util';
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Separator } from '@/components/ui/separator';
import { Button } from '@/components/ui/button';

const dateTimeFormat = dateTimeFormatUtil({
  day: 'numeric',
  month: 'long',
  year: 'numeric',
});

export const Route = createFileRoute('/offers/$offerId/')({
  component: OfferItemDetails,
});

function OfferItemDetails() {
  const offerId = Route.useParams({
    select: (params) => parseInt(params.offerId),
  });

  const { data: offer, isPending } = $olxApi.useQuery(
    'get',
    '/api/offers/{offerId}',
    { params: { path: { offerId } } },
  );

  if (isPending) {
    return <div></div>;
  }

  return (
    <main className="min-h-screen py-6 px-4">
      <div className="max-w-[1200px] mx-auto">
        <div>
          <Button
            variant="ghost"
            asChild
            className="hover:bg-transparent hover:text-neutral-600 text-neutral-800"
          >
            <Link to="/offers">
              <ChevronLeft className="h-4 w-4" />
              Go back
            </Link>
          </Button>
        </div>
        <div className="flex flex-col gap-3 md:flex-row mt-6">
          <div className="md:w-1/3 md:order-2">
            <Card>
              <CardContent className="space-y-4">
                <div>
                  {offer?.createdAt && (
                    <span className="text-xs text-neutral-500">
                      Added {dateTimeFormat.format(new Date(offer.createdAt))}
                    </span>
                  )}
                </div>
                <h1 className="text-2xl font-medium text-neutral-900 leading-snug">
                  {offer?.title}
                </h1>
                {offer?.price && (
                  <div className="text-3xl font-bold text-neutral-900">
                    {priceFormatUtil.format(offer.price)}
                  </div>
                )}
              </CardContent>
            </Card>
          </div>
          <div className="md:w-2/3">
            <Card>
              <CardHeader>
                <CardTitle className="uppercase text-xl font-bold text-neutral-800">
                  Description
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-neutral-800 leading-relaxed whitespace-pre-wrap text-base">
                  {offer?.description}
                </div>
              </CardContent>
              <Separator />
              <CardFooter className="text-xs text-neutral-500">
                <span>ID: {offer?.id}</span>
              </CardFooter>
            </Card>
          </div>
        </div>
      </div>
    </main>
  );
}
