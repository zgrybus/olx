import { createFileRoute } from '@tanstack/react-router';
import { OfferItem } from '@/features/offers/offer-item/offer-item';

export const Route = createFileRoute('/offers/')({
  component: Offers,
});

const mockOffers = [
  {
    price: '450',
    title: 'iPhone 14 Pro, 256GB, Deep Purple, Like New',
    description:
      'Selling my iPhone 14 Pro, 256GB model. Deep purple color. Perfect condition, no scratches. Battery health at 98%. Comes with original box and cable. Selling due to upgrade. Only serious buyers, no swaps.',
    createdAt: '2025-11-13T09:15:00Z',
    updatedAt: '2025-11-13T09:15:00Z',
  },
  {
    price: '450',
    title: 'Audi A4 B8 2.0 TDI, 2014, Low Mileage, S-Line',
    description:
      'For sale: Audi A4 B8, 2014, 2.0 TDI engine. S-Line package, automatic gearbox. Only 130,000 km mileage. Full service history. No accidents. Great condition inside and out. Comes with two sets of tires. Private seller.',
    createdAt: '2025-11-12T18:30:00Z',
    updatedAt: '2025-11-12T18:30:00Z',
  },
  {
    price: '450',
    title: '2-bedroom apartment for rent, City Center, 50m2',
    description:
      'Spacious 2-bedroom (50m2) apartment available for rent in the city center (ul. Jasna). Fully furnished, high standard. Separate kitchen, balcony with a great view. 3rd floor with elevator. Close to metro. Rent: 3500 PLN + utilities. Deposit required. Available from December 1st.',
    createdAt: '2025-11-12T15:00:00Z',
    updatedAt: '2025-11-13T11:20:00Z',
  },
  {
    price: '450',
    title: 'Sony PlayStation 5 (PS5) Disc Edition + 2 Controllers',
    description:
      'Used PS5 Disc Edition for sale. Console is in excellent working condition. Includes two (2) DualSense controllers, all original cables (HDMI, power), and the stand. No original box. Games not included. Pickup in person only.',
    createdAt: '2025-11-11T21:10:00Z',
    updatedAt: '2025-11-11T21:10:00Z',
  },
  {
    price: '450',
    title: 'Junior Frontend Developer (React) - Remote Job',
    description:
      'We are a fast-growing startup looking for a motivated Junior Frontend Developer with 1+ year of experience in React and TypeScript. 100% remote work. We offer a great team, flexible hours, and a B2B contract. Please send your CV and GitHub portfolio.',
    createdAt: '2025-11-10T10:00:00Z',
    updatedAt: '2025-11-10T10:00:00Z',
  },
  {
    title: 'Free: Wooden Wardrobe, good condition, pickup only',
    description:
      'Giving away a large 3-door wooden wardrobe for free due to moving. Dimensions: 180cm (width) x 200cm (height) x 60cm (depth). Good condition, some minor scratches. Must be picked up this weekend (self-transport and carrying).',
    createdAt: '2025-11-09T14:00:00Z',
    updatedAt: '2025-11-12T08:00:00Z',
    price: '450',
  },
  {
    price: '450',
    title: 'Math Tutoring - High School Exam Prep',
    description:
      'Experienced math teacher (10+ years) offering tutoring for high school students. Specializing in preparation for the final exams (basic and advanced levels). I focus on understanding, not just memorizing. Online (Zoom) or in-person. 80 PLN / 60 min.',
    createdAt: '2025-11-08T17:45:00Z',
    updatedAt: '2025-11-08T17:45:00Z',
  },
  {
    price: '450',
    title: 'Used IKEA KALLAX Shelf (4x4, White)',
    description:
      'Selling a white IKEA KALLAX 4x4 shelving unit. Good condition, very stable. Some small signs of use. Perfect for books or vinyl records. Dimensions: 147x147 cm. Selling assembled. Pickup from city center.',
    createdAt: '2025-11-07T13:22:00Z',
    updatedAt: '2025-11-07T13:22:00Z',
  },
  {
    price: '450',
    title: 'KTM Duke 390 Motorcycle, 2019, Akrapovic exhaust',
    description:
      'Selling my 2019 KTM Duke 390. Great bike for the city. Low mileage (8,500 km). Fitted with an original Akrapovic exhaust (great sound). Never dropped, garage kept. Service book included. Selling to buy a bigger bike.',
    createdAt: '2025-11-06T19:50:00Z',
    updatedAt: '2025-11-10T12:30:00Z',
  },
  {
    price: '450',
    title: 'Canon EOS R6 Camera Body + EF-RF Adapter',
    description:
      'Selling my Canon EOS R6 mirrorless camera body. Shutter count approx 15k. Excellent condition, works perfectly. Includes 2 batteries, charger, and the original Canon EF-RF mount adapter. Amazing camera for both photo and video.',
    createdAt: '2025-11-05T11:00:00Z',
    updatedAt: '2025-11-05T11:00:00Z',
  },
];

function Offers() {
  return (
    <main className="py-5 px-2 text-xl font-bold bg-zinc-100">
      <div className="max-w-[1200px] mx-auto">
        <p>We found {mockOffers.length} offers</p>
        <ul className="flex flex-col gap-4 mt-8">
          {mockOffers.map((offer) => (
            <OfferItem key={offer.title} {...offer} />
          ))}
        </ul>
      </div>
    </main>
  );
}
