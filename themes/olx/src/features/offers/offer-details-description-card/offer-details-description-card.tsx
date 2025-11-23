import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card';
import { Separator } from '@/components/ui/separator';

type OfferDetailsDescriptionCardProps = {
  id: number | undefined;
  description: string | undefined;
};

export const OfferDetailsDescriptionCard = ({
  id,
  description,
}: OfferDetailsDescriptionCardProps) => {
  return (
    <Card data-testid="offer-details-description-card">
      <CardHeader>
        <CardTitle className="text-xl font-bold text-neutral-800 uppercase">
          Description
        </CardTitle>
      </CardHeader>
      <CardContent>
        <div
          className={`
            text-base leading-relaxed whitespace-pre-wrap text-neutral-800
          `}
        >
          {description}
        </div>
      </CardContent>
      <Separator />
      <CardFooter className="text-xs text-neutral-500">
        <span>ID: {id}</span>
      </CardFooter>
    </Card>
  );
};
